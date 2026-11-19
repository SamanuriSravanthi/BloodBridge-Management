package com.savelife.blood_donation.dto;

import com.savelife.blood_donation.entity.BloodGroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DonorProfileForm {
	@NotNull
	private BloodGroup bloodGroup;

	@NotBlank
	@Size(max = 100)
	private String city;

	@NotBlank
	@Pattern(regexp = "^[0-9+() .-]{7,20}$")
	private String phone;

	private boolean available = true;

	public BloodGroup getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(BloodGroup bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
}
