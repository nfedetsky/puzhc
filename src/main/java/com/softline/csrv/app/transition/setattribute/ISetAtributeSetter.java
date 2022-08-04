package com.softline.csrv.app.transition.setattribute;

import com.softline.csrv.app.transition.model.RequestFlowParams;

/**
 * Сервис для установки значений атрибутов
 */

public interface ISetAtributeSetter {
    public void execute(RequestFlowParams params);
}