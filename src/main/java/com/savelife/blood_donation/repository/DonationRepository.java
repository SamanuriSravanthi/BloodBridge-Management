package com.savelife.blood_donation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.savelife.blood_donation.entity.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {
	List<Donation> findByDonorProfileUserIdOrderByDonationDateDesc(Long userId);
}
