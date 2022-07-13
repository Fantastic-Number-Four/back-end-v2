package com.revature.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="watchlist")
@Data @AllArgsConstructor@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@EqualsAndHashCode(exclude= {"watchlist"}) @ToString(exclude= {"watchlist"})
public class Watchlist {
	@Id
	@Column(name="watchlist_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private int userId;
//	@OneToMany(mappedBy="watchlist")
//	private Set<CurrencyPair> watchlist;
//	
	@OneToMany
	@JoinTable(name = "users_watchlist", 
	joinColumns = @JoinColumn(name = "watchlist_id"), 
	inverseJoinColumns = @JoinColumn(name = "currencypair_id"))
	private Set<CurrencyPair> watchlist;
	

}
