import React, { useEffect, useState } from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';
import { addTranslationSourcePrefix } from 'app/shared/reducers/locale';
import { useAppDispatch, useAppSelector } from 'app/config/store';

const MedicalUserMenu = () => {
  const lastChange = useAppSelector(state => state.locale.lastChange);
  const dispatch = useAppDispatch();
  useEffect(() => {
    dispatch(addTranslationSourcePrefix('services/laboratory/'));
  }, [lastChange]);

  return (
    <>
      <MenuItem icon="asterisk" to="/laboratory/service-request">
        <Translate contentKey="global.menu.entities.laboratoryServiceRequest" />
      </MenuItem>
    </>
  );
};

export default MedicalUserMenu;
