package com.richards.mealsapp.event;

import com.richards.mealsapp.entity.Person;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ApplicationContextEvent;

@Getter
@Setter
public class RegistrationEvent extends ApplicationEvent {
    private Person person;
    private String applicatiionUrl;

    public RegistrationEvent(Person person, String applicationUrl) {
        super(person);

        this.person = person;
        this.applicatiionUrl = applicationUrl;

    }
}
