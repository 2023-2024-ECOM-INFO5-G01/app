
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities,getPatientSearch ,getPatientsByUserId} from 'app/entities/patient/patient.reducer';
import { getRappelforUser } from 'app/entities/rappel/rappel.reducer';

export const Rappels = () => {
  const account = useAppSelector(state => state.authentication.account);

  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const [rappelsData, setrappelsData] = useState([]);
 

  useEffect(() => {
    if (account && account.login) {
      dispatch(getRappelforUser(account.login))
        .then(response => {
         setrappelsData((response.payload as any).data);
          console.log(rappelsData); 
        })
        .catch(error => {
          console.error('Une erreur s\'est produite :', error);
        });
    }
  }, [account]);

  return (
    <div>
      <h1>Rappels</h1>
      <ul>
        {rappelsData.map((rappel, index) => (
          <li key={index}>
            <div>Action: {rappel.action}</div>
            <div>Date: {rappel.date}</div>
            <div>Patient: {rappel.patient.nom} {rappel.patient.prenom}</div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Rappels;
