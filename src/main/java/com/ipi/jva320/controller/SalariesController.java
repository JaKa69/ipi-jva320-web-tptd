package com.ipi.jva320.controller;

import com.ipi.jva320.exception.SalarieException;
import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.service.DataInitService;
import com.ipi.jva320.service.SalarieAideADomicileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/salaries")
public class SalariesController {

    @Autowired
    private SalarieAideADomicileService salarieAideADomicileService;

    @GetMapping("/{id}")
    private String salariesDetails(ModelMap model,@PathVariable(value = "id") String id) {
        SalarieAideADomicile salarie = salarieAideADomicileService.getSalarie(Long.valueOf(id));
        model.put("nbSalaries", salarieAideADomicileService.countSalaries());
        model.put("salarieDetail", salarie);
        return "detail_Salarie";
    }

    @GetMapping("")
    private String list(ModelMap model,
                        @RequestParam(name = "nom", required = false) String nom,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "10") int size,
                        @RequestParam(name = "sortProperty", defaultValue = "id") String sortProperty,
                        @RequestParam(name = "sortDirection", defaultValue = "ASC") String sortDirection) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortProperty));

        List<SalarieAideADomicile> listeSalarie;

        if (nom != null && !nom.isEmpty()) {
            listeSalarie = salarieAideADomicileService.getSalaries(nom);
        } else {
            listeSalarie = salarieAideADomicileService.getSalaries(pageable).toList();
        }
        model.put("listeSalarie", listeSalarie);
        model.put("nbSalaries", salarieAideADomicileService.countSalaries());
        int lastPage = salarieAideADomicileService.getPageNum();
        if (page != lastPage) {
            salarieAideADomicileService.setPageNum(page);
        }
        model.put("pageNum", page);
        return "list";
    }

    @GetMapping("/aide/new")
    private String getSalarieCreationPage(ModelMap model) {
        List<SalarieAideADomicile> allSalarie = salarieAideADomicileService.getSalaries();
        SalarieAideADomicile newSalarie = new SalarieAideADomicile();
        Optional<Long> lastId = allSalarie.stream()
                .map(SalarieAideADomicile::getId)
                .max(Comparator.naturalOrder());
        if (allSalarie.isEmpty()) {
            lastId = Optional.of(0L);
        }
        newSalarie.setId(lastId.get() + 1);
        model.put("salarieDetail", newSalarie);
        model.put("nbSalaries", salarieAideADomicileService.countSalaries());

        return "detail_Salarie";
    }

    @PostMapping("/save-salarie")
    private String createOrUpdateSalarie(ModelMap model, SalarieAideADomicile salarieAideADomicile) throws SalarieException {
        SalarieAideADomicile salarie = salarieAideADomicileService.saveOrUpdateSalarie(salarieAideADomicile);
        model.put("nbSalaries", salarieAideADomicileService.countSalaries());
        model.put("salarieDetail", salarie);
        return "redirect:/salaries";
    }

    @GetMapping("/{salarieId}/delete")
    public String deleteSalarie(@PathVariable Long salarieId) throws SalarieException {
        salarieAideADomicileService.deleteSalarieAideADomicile(salarieId);

        return "redirect:/salaries";
    }
}
