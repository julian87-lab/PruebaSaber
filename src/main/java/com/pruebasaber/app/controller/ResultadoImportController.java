package com.pruebasaber.app.controller;

import com.pruebasaber.app.service.ExcelImportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/resultados")
public class ResultadoImportController {

    private final ExcelImportService excelImportService;

    public ResultadoImportController(ExcelImportService excelImportService) {
        this.excelImportService = excelImportService;
    }

    @PostMapping("/importar")
    public String importar(@RequestParam("archivo") MultipartFile archivo,
                           @RequestParam("periodo") String periodo,
                           RedirectAttributes redirectAttributes) {
        try {
            if (archivo == null || archivo.isEmpty()) {
                redirectAttributes.addFlashAttribute("mensajeError", "Debes seleccionar un archivo Excel.");
                return "redirect:/resultados";
            }

            int total = excelImportService.importarResultados(archivo, periodo);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Importación completada. Registros importados: " + total);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error al importar el archivo: " + e.getMessage());
        }

        return "redirect:/resultados";
    }
}