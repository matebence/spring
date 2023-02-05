package com.bence.mate.spring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.bence.mate.spring.model.Holiday;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
public class HolidaysController {

	// @GetMapping("/holiday")
	@RequestMapping(value = "holiday")
	public String displayHolidays(@RequestParam(required = false) boolean festival, @RequestParam(required = false) boolean federal, Model model) {
		model.addAttribute("festival", festival);
		model.addAttribute("federal", federal);
		
		List<Holiday> holidays = Arrays.asList(new Holiday(" Jan 1 ", "New Year's Day", Holiday.Type.FESTIVAL),
				new Holiday(" Oct 31 ", "Halloween", Holiday.Type.FESTIVAL),
				new Holiday(" Nov 24 ", "Thanksgiving Day", Holiday.Type.FESTIVAL),
				new Holiday(" Dec 25 ", "Christmas", Holiday.Type.FESTIVAL),
				new Holiday(" Jan 17 ", "Martin Luther King Jr. Day", Holiday.Type.FEDERAL),
				new Holiday(" July 4 ", "Independence Day", Holiday.Type.FEDERAL),
				new Holiday(" Sep 5 ", "Labor Day", Holiday.Type.FEDERAL),
				new Holiday(" Nov 11 ", "Veterans Day", Holiday.Type.FEDERAL));
		
		Holiday.Type[] types = Holiday.Type.values();
		
		for (Holiday.Type type : types) {
			model.addAttribute(type.toString(),
					(holidays.stream().filter(holiday -> holiday.getType().equals(type)).collect(Collectors.toList())));
		}
		
		return "holiday";
	}

	// @GetMapping("/holiday/{display}")
	@RequestMapping(value = "/holiday/{display}")
	public String displayHolidays(@PathVariable String display, Model model) {
		if (null != display && display.equals("all")) {
			model.addAttribute("festival", true);
			model.addAttribute("federal", true);
		} else if (null != display && display.equals("federal")) {
			model.addAttribute("federal", true);
		} else if (null != display && display.equals("festival")) {
			model.addAttribute("festival", true);
		}
		
		List<Holiday> holidays = Arrays.asList(new Holiday(" Jan 1 ", "New Year's Day", Holiday.Type.FESTIVAL),
				new Holiday(" Oct 31 ", "Halloween", Holiday.Type.FESTIVAL),
				new Holiday(" Nov 24 ", "Thanksgiving Day", Holiday.Type.FESTIVAL),
				new Holiday(" Dec 25 ", "Christmas", Holiday.Type.FESTIVAL),
				new Holiday(" Jan 17 ", "Martin Luther King Jr. Day", Holiday.Type.FEDERAL),
				new Holiday(" July 4 ", "Independence Day", Holiday.Type.FEDERAL),
				new Holiday(" Sep 5 ", "Labor Day", Holiday.Type.FEDERAL),
				new Holiday(" Nov 11 ", "Veterans Day", Holiday.Type.FEDERAL));
		
		Holiday.Type[] types = Holiday.Type.values();
		
		for (Holiday.Type type : types) {
			model.addAttribute(type.toString(),
					(holidays.stream().filter(holiday -> holiday.getType().equals(type)).collect(Collectors.toList())));
		}
		
		return "holiday";
	}
}