import React, { useEffect } from 'react';
import { translate } from 'react-jhipster';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import { addTranslationSourcePrefix } from 'app/shared/reducers/locale';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import { Link } from 'react-router-dom';
import { ListItemIcon } from '@mui/material';
import SummarizeIcon from '@mui/icons-material/Summarize';
import ListAltIcon from '@mui/icons-material/ListAlt';
import MedicalInformationIcon from '@mui/icons-material/MedicalInformation';

const EntitiesMenu = ({ closeMenu }) => {
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
        <ListItemButton component={Link} to="/laboratory/diagnostic-report-format" onClick={closeMenu}>
          <ListItemIcon>
            <SummarizeIcon />
          </ListItemIcon>
          <ListItemText primary={translate('global.menu.entities.laboratoryDiagnosticReportFormat')} />
        </ListItemButton>
        <ListItemButton component={Link} to="/laboratory/value-set" onClick={closeMenu}>
          <ListItemIcon>
            <ListAltIcon />
          </ListItemIcon>
          <ListItemText primary={translate('global.menu.entities.laboratoryValueSet')} />
        </ListItemButton>
      </>
    );
  if (isMedUser)
    return (
      <>
        <ListItemButton component={Link} to="/laboratory/service-request" onClick={closeMenu}>
          <ListItemIcon>
            <MedicalInformationIcon />
          </ListItemIcon>
          <ListItemText primary={translate('global.menu.entities.laboratoryServiceRequest')} />
        </ListItemButton>
      </>
    );
  if (isLabUser)
    return (
      <>
        <ListItemButton component={Link} to="/laboratory/service-request" onClick={closeMenu}>
          <ListItemIcon>
            <MedicalInformationIcon />
          </ListItemIcon>
          <ListItemText primary={translate('global.menu.entities.laboratoryServiceRequest')} />
        </ListItemButton>
      </>
    );
};

export default EntitiesMenu;
