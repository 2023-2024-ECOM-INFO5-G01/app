
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities,getPatientSearch ,getPatientsByUserId} from 'app/entities/patient/patient.reducer';
import { getRappelforUser } from 'app/entities/rappel/rappel.reducer';

export const Notes = () => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
   // dispatch(getEntity(id));
  }, []);

  const patientEntity = useAppSelector(state => state.patient.entity);

  const location = useLocation();
  const navigate = useNavigate();
  const [noteData, setnoteData] = useState([]);
 

  return (
    <div>
      <h1>Rappels</h1>
    </div>
  );
};

export default Notes;
