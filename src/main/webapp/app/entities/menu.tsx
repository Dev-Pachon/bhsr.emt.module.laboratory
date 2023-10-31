import React, { useEffect, useState } from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';
import { addTranslationSourcePrefix } from 'app/shared/reducers/locale';
import { useAppDispatch, useAppSelector } from 'app/config/store';

const EntitiesMenu = () => {
  const lastChange = useAppSelector(state => state.locale.lastChange);
  const dispatch = useAppDispatch();
  useEffect(() => {
    dispatch(addTranslationSourcePrefix('services/laboratory/'));
  }, [lastChange]);

  return (
    <>
      <MenuItem icon="asterisk" to="/laboratory/identifier-type">
        <Translate contentKey="global.menu.entities.laboratoryIdentifierType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/laboratory/diagnostic-report-format">
        <Translate contentKey="global.menu.entities.laboratoryDiagnosticReportFormat" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/laboratory/service-request">
        <Translate contentKey="global.menu.entities.laboratoryServiceRequest" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/laboratory/value-set">
        <Translate contentKey="global.menu.entities.laboratoryValueSet" />
      </MenuItem>
    </>
  );
};

export default EntitiesMenu;
