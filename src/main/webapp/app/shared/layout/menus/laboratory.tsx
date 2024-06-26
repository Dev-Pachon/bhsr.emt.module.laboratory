import React, { Suspense } from 'react';
import { NavDropdown } from './menu-components';

const LaboratoryMenuItems = React.lazy(() => import('app/entities/menu').catch(() => import('app/shared/error/error-loading')));

export const LaboratoryMenu = () => (
  <NavDropdown icon="th-list" name={'Laboratory'} id="lab-menu" data-cy="lab" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <Suspense fallback={<div>loading...</div>}></Suspense>
  </NavDropdown>
);
