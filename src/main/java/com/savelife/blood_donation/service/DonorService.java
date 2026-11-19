package com.savelife.blood_donation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.savelife.blood_donation.dto.DonationForm;
import com.savelife.blood_donation.dto.DonorProfileForm;
import com.savelife.blood_donation.entity.Donation;
import com.savelife.blood_donation.entity.DonorProfile;
import com.savelife.blood_donation.entity.User;
import com.savelife.blood_donation.repository.DonationRepository;
import com.savelife.blood_donation.repository.DonorProfileRepository;
import com.savelife.blood_donation.repository.UserRepository;

@Service
public class DonorService {
	private final UserRepository users;
	private final DonorProfileRepository profiles;
	private final DonationRepository donations;

	public DonorService(UserRepository users, DonorProfileRepository profiles, DonationRepository donations) {
		this.users = users;
		this.profiles = profiles;
		this.donations = donations;
	}

	@Transactional(readOnly = true)
	public DonorProfile getProfile(String email) {
		User user = findUser(email);
		return profiles.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalStateException("Donor profile is missing for this account."));
	}

	@Transactional(readOnly = true)
	public List<Donation> getDonations(String email) {
		User user = findUser(email);
		return donations.findByDonorProfileUserIdOrderByDonationDateDesc(user.getId());
	}

	@Transactional
	public DonorProfile updateProfile(String email, DonorProfileForm form) {
		DonorProfile profile = getProfile(email);
		profile.setBloodGroup(form.getBloodGroup());
		profile.setCity(form.getCity().trim());
		profile.setPhone(form.getPhone().trim());
		profile.setAvailable(form.isAvailable());
		return profiles.save(profile);
	}

	@Transactional
	public Donation recordDonation(String email, DonationForm form) {
		DonorProfile profile = getProfile(email);
		Donation donation = new Donation();
		donation.setDonorProfile(profile);
		donation.setDonationDate(form.getDonationDate());
		donation.setUnits(form.getUnits());
		return donations.save(donation);
	}

	private User findUser(String email) {
		return users.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new IllegalStateException("Signed-in account could not be found."));
	}
}
