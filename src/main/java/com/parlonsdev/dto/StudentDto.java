package com.parlonsdev.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.parlonsdev.entities.Training;

public class StudentDto {
	
	    @NotBlank
	    @Size(max = 25)
	    private String firstName;
	    @NotBlank
	    @Size(max = 25)
	    private String lastName;
	    @NotBlank
	    @Size(max = 80)
	    private String email;
	    @NotBlank
	    @Size(max = 20)
	    private String phone;
	    private Date dob;
	    private String imageUrl;
	    private String address;
	    private Character gender;
	    private boolean registered;
	    private Training training;
	    
		public StudentDto() {}

		public StudentDto(String firstName, String lastName, String email, String phone, Date dob, String imageUrl,
				                                      String address, Character gender, boolean registered, Training training) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.phone = phone;
			this.dob = dob;
			this.imageUrl = imageUrl;
			this.address = address;
			this.gender = gender;
			this.registered = registered;
			this.training = training;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public Date getDob() {
			return dob;
		}

		public void setDob(Date dob) {
			this.dob = dob;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public Character getGender() {
			return gender;
		}

		public void setGender(Character gender) {
			this.gender = gender;
		}

		public boolean isRegistered() {
			return registered;
		}

		public void setRegistered(boolean registered) {
			this.registered = registered;
		}

		public Training getTraining() {
			return training;
		}

		public void setTraining(Training training) {
			this.training = training;
		}
		
		
}
