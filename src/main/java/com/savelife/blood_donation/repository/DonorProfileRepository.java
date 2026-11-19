package com.savelife.blood_donation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.savelife.blood_donation.entity.BloodGroup;
import com.savelife.blood_donation.entity.DonorProfile;

public interface DonorProfileRepository extends JpaRepository<DonorProfile, Long> {
	@EntityGraph(attributePaths = "user")
	Optional<DonorProfile> findByUserId(Long userId);

	List<DonorProfile> findByAvailableTrueAndBloodGroupAndCityIgnoreCase(BloodGroup bloodGroup, String city);
}
