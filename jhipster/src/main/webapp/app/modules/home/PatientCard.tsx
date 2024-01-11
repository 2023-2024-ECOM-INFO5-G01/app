// PatientCard.tsx
import { Row, Col, Alert, Button, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { faSort, faSortUp, faSortDown, faStarAndCrescent, faBell } from '@fortawesome/free-solid-svg-icons';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import React, {useEffect, useState} from "react";
import {useAppDispatch, useAppSelector} from "app/config/store";
import {IAlerte} from "app/shared/model/alerte.model";
import {createEntity} from "app/entities/alerte/alerte.reducer";
import {useForm} from "react-hook-form";
import { getIMC } from "app/entities/imc/imc.reducer";
import { getPoids } from "app/entities/poids/poids.reducer";
import { getEpas } from "app/entities/epa/epa.reducer";
import {IPoids} from "app/shared/model/poids.model";
import {IIMC} from "app/shared/model/imc.model";
import {IEPA} from "app/shared/model/epa.model";
import { PayloadAction } from "@reduxjs/toolkit";

const PatientCard = ({ patient,alertes }) => {
  const patientAlertes = alertes.filter(alerte => alerte.patient.id === patient.id);

  const dispatch = useAppDispatch()

  const [lastEpa, setLastEpa] = useState<IEPA | null>(null);

  useEffect(() => {
    if (patient.id) {
      dispatch(getEpas(patient.id)).then((response: PayloadAction<any>) => {
        if (response.payload && response.payload.data && response.payload.data.length > 0) {
          setLastEpa(response.payload.data[response.payload.data.length - 1]);
        }
      });
    }
  }, [dispatch, patient.id]);

  return (
    <div className='whole-card'>
      <div className="patient-info">
        <div style={{ display: 'flex'}}>
          <span className="category"> Patient : </span>
          <span style={{ marginTop: '2px'}}> {patient.nom} {patient.prenom} </span>
        </div>
        <div style={{ display: 'flex'}}>
          <span className="category"> EPA : </span>
          <span style={{ marginTop: '2px'}}> {lastEpa ? lastEpa.epa : 'Aucune donnée'} </span>
        </div>
        <Button tag={Link} to={`/patients/${patient.id}`} color="primary" size="sm" data-cy="entityDetailsButton" className="rappel-button">
          <FontAwesomeIcon icon="eye" />
        </Button>
      </div>
      {patientAlertes.map((alerte, index) => (
        <div>
            <div className='Alerte_div' key={index}>
              {patientAlertes.length > 0 && (<FontAwesomeIcon icon={faBell} className="blinking-icon" />)}
              <span style={{marginLeft: '5px'}}>Alertes : {alerte.action}</span>
            </div>
        </div>
      ))}
    </div>
  );
};

export default PatientCard;