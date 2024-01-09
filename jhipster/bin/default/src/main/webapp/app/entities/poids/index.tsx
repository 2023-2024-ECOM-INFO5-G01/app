import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Poids from './poids';
import PoidsDetail from './poids-detail';
import PoidsUpdate from './poids-update';
import PoidsDeleteDialog from './poids-delete-dialog';

const PoidsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Poids />} />
    <Route path="new" element={<PoidsUpdate />} />
    <Route path=":id">
      <Route index element={<PoidsDetail />} />
      <Route path="edit" element={<PoidsUpdate />} />
      <Route path="delete" element={<PoidsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PoidsRoutes;
