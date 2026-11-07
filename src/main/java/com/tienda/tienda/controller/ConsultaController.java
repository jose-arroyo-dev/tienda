package com.tienda.tienda.controller;

import com.tienda.tienda.service.ProductoService;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private final ProductoService productoService;
    private final com.tienda.tienda.service.CategoriaService categoriaService;

    public ConsultaController(ProductoService productoService, com.tienda.tienda.service.CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var productos = productoService.getProductos(false);
        model.addAttribute("productos", productos);
        return "/consultas/listado";
    }

    @PostMapping("/consultaDerivada")
    public String consultaDerivida(@RequestParam() BigDecimal precioInf,
            @RequestParam() BigDecimal precioSup,
            Model model) {
        var productos = productoService.consultaDerivada(precioInf, precioSup);
        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
    }

    @PostMapping("/consultaJPQL")
    public String consultaJPQL(@RequestParam() BigDecimal precioInf,
            @RequestParam() BigDecimal precioSup,
            Model model) {
        var productos = productoService.consultaJPQL(precioInf, precioSup);
        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
    }

    @PostMapping("/consultaSQL")
    public String consultaSQL(@RequestParam() BigDecimal precioInf,
            @RequestParam() BigDecimal precioSup,
            Model model) {
        var productos = productoService.consultaSQL(precioInf, precioSup);
        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
    }
    @org.springframework.web.bind.annotation.ModelAttribute("categoriasFiltro")
    public java.util.List<com.tienda.tienda.domain.Categoria> categoriasFiltro() {
        return categoriaService.getCategorias(false);
    }

    @GetMapping("/ampliada")
    public String ampliada(@RequestParam(required = false) Integer categoria,
            @RequestParam BigDecimal minimo, @RequestParam BigDecimal maximo,
            @RequestParam(defaultValue = "0") Integer existencias, Model model) {
        model.addAttribute("categoriaFiltro", categoria);
        model.addAttribute("minimo", minimo);
        model.addAttribute("maximo", maximo);
        model.addAttribute("existenciasFiltro", existencias);
        if (minimo.signum() < 0 || maximo.compareTo(minimo) < 0 || existencias < 0) {
            model.addAttribute("errorConsulta", "Revise el rango de precios y las existencias: deben ser positivos y el precio superior debe ser mayor o igual al inferior.");
            model.addAttribute("productos", java.util.List.of());
        } else {
            model.addAttribute("productos", productoService.consultaAmpliada(categoria, minimo, maximo, existencias));
            model.addAttribute("consultaAmpliada", true);
        }
        return "/consultas/listado";
    }
}