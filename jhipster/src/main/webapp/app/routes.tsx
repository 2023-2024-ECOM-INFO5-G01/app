import React from 'react';
import { Route } from 'react-router-dom';
import Loadable from 'react-loadable';

import Login from 'app/modules/login/login';
import Register from 'app/modules/account/register/register';
import Activate from 'app/modules/account/activate/activate';
import PasswordResetInit from 'app/modules/account/password-reset/init/password-reset-init';
import PasswordResetFinish from 'app/modules/account/password-reset/finish/password-reset-finish';
import Logout from 'app/modules/login/logout';
import Home from 'app/modules/home/home';
import EntitiesRoutes from 'app/entities/routes';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PageNotFound from 'app/shared/error/page-not-found';
import { AUTHORITIES } from 'app/config/constants';
import Rappels from './shared/layout/menus/rappels';
import Alerte from './shared/layout/menus/alerte';
import Patient from './PatientView/patient';
import Notes from './note';

import AlertePatient from './alertespatient';
import PatientUpdate from './entities/patient/patient-update';
import PatientEdit from './PatientView/patient_edit';
import Confidentiality from './modules/account/register/Confidentiality';
import Consent from './modules/account/register/Consent';

const loading = <div>loading ...</div>;

const Account = Loadable({
  loader: () => import(/* webpackChunkName: "account" */ 'app/modules/account'),
  loading: () => loading,
});

const Admin = Loadable({
  loader: () => import(/* webpackChunkName: "administration" */ 'app/modules/administration'),
  loading: () => loading,
});

const AppRoutes = () => {
  return (
    <div className="view-routes">
      <ErrorBoundaryRoutes>
        <Route index  element={
          <PrivateRoute hasAnyAuthorities={[AUTHORITIES.USER, AUTHORITIES.MEDECIN, AUTHORITIES.ADMIN,AUTHORITIES.SOIGNANT]}>
            <Home />
          </PrivateRoute>
        } />
        <Route path="rappels" element={
          <PrivateRoute hasAnyAuthorities={[AUTHORITIES.USER, AUTHORITIES.MEDECIN, AUTHORITIES.ADMIN,AUTHORITIES.SOIGNANT]}>
            <Rappels />
          </PrivateRoute>
        } />
        <Route path="alertes" element={
          <PrivateRoute hasAnyAuthorities={[AUTHORITIES.USER, AUTHORITIES.MEDECIN, AUTHORITIES.ADMIN]}>
            <Alerte />
          </PrivateRoute>
        } />
        <Route path="patients/:id" element={
          <PrivateRoute hasAnyAuthorities={[AUTHORITIES.USER, AUTHORITIES.MEDECIN, AUTHORITIES.ADMIN,AUTHORITIES.SOIGNANT]}>
            <Patient />
          </PrivateRoute>
        } />
   
        <Route path="login" element={<Login />} />
        <Route path="logout" element={<Logout />} />
        <Route path="account">
          <Route
            path="*"
            element={
              <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER, AUTHORITIES.MEDECIN, AUTHORITIES.SOIGNANT]}>
                <Account />
              </PrivateRoute>
            }
          />
          <Route path="consent" element={<Consent />} />
          <Route path="privacy-policy" element={<Confidentiality />} />
          <Route path="register" element={<Register />} />
          <Route path="activate" element={<Activate />} />
          <Route path="reset">
            <Route path="request" element={<PasswordResetInit />} />
            <Route path="finish" element={<PasswordResetFinish />} />
          </Route>
        </Route>
        <Route path='patient/new' element ={
          <PrivateRoute hasAnyAuthorities={[AUTHORITIES.MEDECIN, AUTHORITIES.ADMIN]}>
            <PatientUpdate />
          </PrivateRoute>
        } />


        <Route
          path="admin/*"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN]}>
              <Admin />
            </PrivateRoute>
          }
        />
        <Route
          path="*"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.USER, AUTHORITIES.MEDECIN]}>
              <EntitiesRoutes />
            </PrivateRoute>
          }
        />
        <Route path="*" element={<PageNotFound />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};

export default AppRoutes;
