/*
 * Copyright 2010 Impetus Infotech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.impetus.annovention;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Class Author.
 * 
 * @author animesh.kumar
 */
@Entity
@Table(name = "yeah")
public class DummyAnnotatedClass {

	// Annotated Field
	@Id
	String username;

	@Column(name = "email")
	String emailAddress;

	@Column
	String country;

	@Column(name = "registeredSince")
	@Temporal(TemporalType.DATE)
	@Basic
	Date registered;

	@Column
	String name;

	// annotated methods
	@PrePersist
	@PostPersist
	public void callMeAtPreAndPostPersist() {
	}

	@PostPersist
	public void callMeAtPostPersist() {
	}

	@PreRemove
	public List<String> callMeAtPreRemove(List<String> hey) {
		return null;
	}
}
