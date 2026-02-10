package org.cy.micoservice.app.framework.id.starter.service;

import java.util.List;

public interface IdService {

  long getId();

  List<Long> getBatch(int size);
}
