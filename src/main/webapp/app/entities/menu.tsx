import React, { useEffect, useState } from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';
import { addTranslationSourcePrefix } from 'app/shared/reducers/locale';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

const EntitiesMenu = () => {
  const lastChange = useAppSelector(state => state.locale.lastChange);
  const isLabUser = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.LAB]));
  const isMedUser = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.MED]));
  const isAdminUser = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const dispatch = useAppDispatch();
  useEffect(() => {
    dispatch(addTranslationSourcePrefix('services/laboratory/'));
  }, [lastChange]);

  if (isAdminUser)
    return (
      <>
        <MenuItem icon="asterisk" to="/laboratory/diagnostic-report-format">
          <Translate contentKey="global.menu.entities.laboratoryDiagnosticReportFormat" />
        </MenuItem>
        <MenuItem icon="asterisk" to="/laboratory/value-set">
          <Translate contentKey="global.menu.entities.laboratoryValueSet" />
        </MenuItem>
      </>
    );
  if (isMedUser)
    return (
      <>
        <MenuItem icon="asterisk" to="/laboratory/service-request">
          <Translate contentKey="global.menu.entities.laboratoryServiceRequest" />
        </MenuItem>
      </>
    );
  if (isLabUser)
    return (
      <>
        <MenuItem icon="asterisk" to="/laboratory/service-request">
          <Translate contentKey="global.menu.entities.laboratoryServiceRequest" />
        </MenuItem>
      </>
    );
};

export default EntitiesMenu;
