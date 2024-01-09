import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ehpad from './ehpad';
import EhpadDetail from './ehpad-detail';
import EhpadUpdate from './ehpad-update';
import EhpadDeleteDialog from './ehpad-delete-dialog';

const EhpadRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Ehpad />} />
    <Route path="new" element={<EhpadUpdate />} />
    <Route path=":id">
      <Route index element={<EhpadDetail />} />
      <Route path="edit" element={<EhpadUpdate />} />
      <Route path="delete" element={<EhpadDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EhpadRoutes;
