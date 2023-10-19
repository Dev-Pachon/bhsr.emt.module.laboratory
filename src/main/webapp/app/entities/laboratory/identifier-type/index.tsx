import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import IdentifierType from './identifier-type';
import IdentifierTypeDetail from './identifier-type-detail';
import IdentifierTypeUpdate from './identifier-type-update';
import IdentifierTypeDeleteDialog from './identifier-type-delete-dialog';

const IdentifierTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<IdentifierType />} />
    <Route path="new" element={<IdentifierTypeUpdate />} />
    <Route path=":id">
      <Route index element={<IdentifierTypeDetail />} />
      <Route path="edit" element={<IdentifierTypeUpdate />} />
      <Route path="delete" element={<IdentifierTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IdentifierTypeRoutes;
