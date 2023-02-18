package com.richards.mealsapp.event;

import com.richards.mealsapp.entity.Person;
import com.richards.mealsapp.enums.EventType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class MealApplicationEvent extends ApplicationEvent {
    private Person person;
    private String applicationUrl;
    private EventType eventType;

    public MealApplicationEvent(Person person, String applicationUrl, EventType eventType) {
        super(person);

        this.person = person;
        this.applicationUrl = applicationUrl;
        this.eventType = eventType;

    }
}
