package com.savelife.blood_donation.dto;

import com.savelife.blood_donation.entity.BloodGroup;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BloodRequestForm {
	@NotNull
	private BloodGroup bloodGroup;

	@Min(1)
	@Max(20)
	private int units = 1;

	@NotBlank
	@Size(max = 150)
	private String hospitalName;

	@NotBlank
	@Size(max = 100)
	private String city;

	@NotBlank
	@Pattern(regexp = "^[0-9+() .-]{7,20}$")
	private String contactPhone;

	@Size(max = 1000)
	private String message;

	public BloodGroup getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(BloodGroup bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
