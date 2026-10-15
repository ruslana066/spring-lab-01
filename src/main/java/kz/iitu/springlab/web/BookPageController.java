package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.context.MessageSource;
import java.util.Locale;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/books")
public class BookPageController {

    private final BookService service;
    private final MessageSource messageSource;

    public BookPageController(BookService service, MessageSource messageSource) {
        this.service = service;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) String author,
            Model model,
            jakarta.servlet.http.HttpServletRequest request) {

        var resolver = org.springframework.web.servlet.support.RequestContextUtils
                .getLocaleResolver(request);

        Locale locale = resolver.resolveLocale(request);

        System.out.println("LOCALE: " + locale);
        System.out.println("TRANSLATION: " +
                messageSource.getMessage("book.list", null, locale));

        model.addAttribute("books", service.findAll(author));
        model.addAttribute("author", author);

        return "books/list";
    }


    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") BookForm form,
            BindingResult binding,
            Model model,
            RedirectAttributes redirect,
            @RequestParam(defaultValue = "en") String lang) {

        if (binding.hasErrors()) {
            model.addAttribute("genres", Genre.values());
            model.addAttribute("lang", lang);
            return "books/form";
        }

        Book saved = service.create(new Book(
                null,
                form.getTitle(),
                form.getAuthor(),
                form.getYear()
        ));

        redirect.addFlashAttribute(
                "message",
                "Book \"" + saved.title() + "\" has been added"
        );

        redirect.addAttribute("lang", lang);

        return "redirect:/books";
    }

    @GetMapping("/new")
    public String newForm(
            Model model,
            @RequestParam(defaultValue = "en") String lang) {

        model.addAttribute("form", new BookForm());
        model.addAttribute("genres", Genre.values());
        model.addAttribute("lang", lang);

        return "books/form";
    }


    @GetMapping("/{id}")
    public String bookDetails(@PathVariable long id, Model model) {
        model.addAttribute("book", service.findById(id));
        return "books/details";
    }

}