import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Albumine from './albumine';
import AlbumineDetail from './albumine-detail';
import AlbumineUpdate from './albumine-update';
import AlbumineDeleteDialog from './albumine-delete-dialog';

const AlbumineRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Albumine />} />
    <Route path="new" element={<AlbumineUpdate />} />
    <Route path=":id">
      <Route index element={<AlbumineDetail />} />
      <Route path="edit" element={<AlbumineUpdate />} />
      <Route path="delete" element={<AlbumineDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AlbumineRoutes;
