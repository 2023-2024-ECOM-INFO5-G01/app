import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EPA from './epa';
import EPADetail from './epa-detail';
import EPAUpdate from './epa-update';
import EPADeleteDialog from './epa-delete-dialog';

const EPARoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EPA />} />
    <Route path="new" element={<EPAUpdate />} />
    <Route path=":id">
      <Route index element={<EPADetail />} />
      <Route path="edit" element={<EPAUpdate />} />
      <Route path="delete" element={<EPADeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EPARoutes;
