import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Repas from './repas';
import RepasDetail from './repas-detail';
import RepasUpdate from './repas-update';
import RepasDeleteDialog from './repas-delete-dialog';

const RepasRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Repas />} />
    <Route path="new" element={<RepasUpdate />} />
    <Route path=":id">
      <Route index element={<RepasDetail />} />
      <Route path="edit" element={<RepasUpdate />} />
      <Route path="delete" element={<RepasDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RepasRoutes;
