package com.batch.application.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Member {

	/*@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	Integer id;*/

	@Id
	Long aadharNumber;

	String firstName;

	String lastName;

	Date createdTime;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	Address address;

	/*public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}*/

	public Long getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(Long aadharNumber) {
		this.aadharNumber = aadharNumber;
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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	/*@Override
	public String toString() {
		return "Member [id=" + id + ", aadharNumber=" + aadharNumber + ", firstName=" + firstName + ", lastName="
				+ lastName + ", createdTime=" + createdTime + "]";
	}*/
}
