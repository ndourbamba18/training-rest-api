package com.parlonsdev.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

public class TrainingDto {

    @NotBlank
    @Size(max = 50)
    private String name;
    private Date duration;
    private boolean started;

    public TrainingDto() {}

    public TrainingDto(String name, Date duration, boolean started) {
		this.name = name;
		this.duration = duration;
		this.started = started;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}
    
    
}
