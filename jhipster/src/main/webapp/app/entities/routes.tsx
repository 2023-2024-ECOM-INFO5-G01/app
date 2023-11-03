import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EPA from './epa';
import Ehpad from './ehpad';
import Patient from './patient';
import Albumine from './albumine';
import Rappel from './rappel';
import Poids from './poids';
import IMC from './imc';
import Repas from './repas';
import Note from './note';
import Aliment from './aliment';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="epa/*" element={<EPA />} />
        <Route path="ehpad/*" element={<Ehpad />} />
        <Route path="patient/*" element={<Patient />} />
        <Route path="albumine/*" element={<Albumine />} />
        <Route path="rappel/*" element={<Rappel />} />
        <Route path="poids/*" element={<Poids />} />
        <Route path="imc/*" element={<IMC />} />
        <Route path="repas/*" element={<Repas />} />
        <Route path="note/*" element={<Note />} />
        <Route path="aliment/*" element={<Aliment />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
