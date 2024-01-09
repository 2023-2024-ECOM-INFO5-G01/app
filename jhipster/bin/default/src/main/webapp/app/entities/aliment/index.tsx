import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Aliment from './aliment';
import AlimentDetail from './aliment-detail';
import AlimentUpdate from './aliment-update';
import AlimentDeleteDialog from './aliment-delete-dialog';

const AlimentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Aliment />} />
    <Route path="new" element={<AlimentUpdate />} />
    <Route path=":id">
      <Route index element={<AlimentDetail />} />
      <Route path="edit" element={<AlimentUpdate />} />
      <Route path="delete" element={<AlimentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AlimentRoutes;
