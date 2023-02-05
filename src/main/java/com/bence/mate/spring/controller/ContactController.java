package com.bence.mate.spring.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.ui.Model;

import com.bence.mate.spring.model.Contact;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ContactController {

	// @GetMapping(value = "/contact")
	@RequestMapping(value = "/contact")
	public String displayContactPage(Model model) {
		model.addAttribute("contact", new Contact());
		return "contact";
	}

	/*
	 * @PostMapping(value = "/saveMsg")
	 * 
	 * @RequestMapping(value = "/saveMsg", method = RequestMethod.POST) public
	 * ModelAndView saveMessage(Contact contact) { instead of the model we could
	 * also use the @RequestParam with the from name values log.info("{}", contact);
	 * return new ModelAndView("redirect:/contact"); }
	 */

	@RequestMapping(value = "/saveMsg", method = RequestMethod.POST)
	public String saveMessage(@Valid @ModelAttribute("contact") Contact contact, Errors errors) {
		// It validates first and then it auto populates back the values to the view its
		// like mode.addObject(contact)
		if (errors.hasErrors()) {
			log.error("Contact form validation failed due to : " + errors.toString());
			return "contact.html";
		}
		log.info("{}", contact);
		return "redirect:/contact";
	}
}
