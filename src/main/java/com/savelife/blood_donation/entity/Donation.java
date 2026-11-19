package com.savelife.blood_donation.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Entity
@Table(name = "donations")
public class Donation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "donor_profile_id", nullable = false)
	private DonorProfile donorProfile;

	@Column(nullable = false)
	private LocalDate donationDate;

	@Min(1)
	@Max(10)
	@Column(nullable = false)
	private int units = 1;

	public Long getId() {
		return id;
	}

	public DonorProfile getDonorProfile() {
		return donorProfile;
	}

	public void setDonorProfile(DonorProfile donorProfile) {
		this.donorProfile = donorProfile;
	}

	public LocalDate getDonationDate() {
		return donationDate;
	}

	public void setDonationDate(LocalDate donationDate) {
		this.donationDate = donationDate;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}
}
