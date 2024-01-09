import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import IMC from './imc';
import IMCDetail from './imc-detail';
import IMCUpdate from './imc-update';
import IMCDeleteDialog from './imc-delete-dialog';

const IMCRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<IMC />} />
    <Route path="new" element={<IMCUpdate />} />
    <Route path=":id">
      <Route index element={<IMCDetail />} />
      <Route path="edit" element={<IMCUpdate />} />
      <Route path="delete" element={<IMCDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IMCRoutes;
