
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities,getPatientSearch ,getPatientsByUserId} from 'app/entities/patient/patient.reducer';

export const Alerte = () => {
  const account = useAppSelector(state => state.authentication.account);

  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

 
  return (
    <div>
        <h1>Alerte</h1>
    </div>
  );
};

export default Alerte;
