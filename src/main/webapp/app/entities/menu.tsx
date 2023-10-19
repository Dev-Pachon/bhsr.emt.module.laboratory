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
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/laboratory/patient">
        <Translate contentKey="global.menu.entities.laboratoryPatient" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/laboratory/identifier-type">
        <Translate contentKey="global.menu.entities.laboratoryIdentifierType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/laboratory/diagnostic-report">
        <Translate contentKey="global.menu.entities.laboratoryDiagnosticReport" />
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
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
