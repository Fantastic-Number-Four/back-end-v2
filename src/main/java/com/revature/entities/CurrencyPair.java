package com.revature.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name="currencypairs")
@Data @AllArgsConstructor @NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@EqualsAndHashCode(exclude= {"users"}) @ToString(exclude= {"users"})
public class CurrencyPair {

	@Id
	@Column(name="currencypair_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String address;
	
	@NonNull
	@ManyToMany(mappedBy="currencyPairs")
	private Set<User> users;
	
}
