import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './entities/patient/patient.reducer';

import { getIMC } from './entities/imc/imc.reducer';
import { getEpas } from './entities/epa/epa.reducer';
import { getPoids } from './entities/poids/poids.reducer';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';

import '../content/css/patient.css';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);
export const Patient = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
    dispatch(getIMC(id)).then((response) => {
      console.log(response);
      if (response.payload && (response.payload as any).data) {
        const imcData = (response.payload as any).data;
        const dates = imcData.map((imc: any) => imc.date);
        const values = imcData.map((imc: any) => imc.imc);
        setImcDates(dates);
        setImcValues(values);
        console.log(dates);
        console.log(values);
      }
    });
    dispatch(getPoids(id)).then((response) => {
      console.log(response);
      if (response.payload && (response.payload as any).data) {
        const poidsData = (response.payload as any).data;
        const dates = poidsData.map((poids: any) => poids.date);
        const values = poidsData.map((poids: any) => poids.poids);
        setPoidDates(dates);
        setPoidValues(values);
        console.log(dates);
        console.log(values);
      }
    });
    dispatch(getEpas(id)).then((response) => {
      console.log(response);
      if (response.payload && (response.payload as any).data) {
        const epaData = (response.payload as any).data;
        const dates = epaData.map((epa: any) => epa.date);
        const values = epaData.map((epa: any) => epa.epa);
        setEpaDates(dates);
        setEpaValues(values);
        console.log(dates);
        console.log(values);
      }
    }
    );
  }, []);
  
  const [imcs, setimcs] = useState([]);
  const [imcDates, setImcDates] = useState<string[]>([]); // Tableau de dates (chaînes)
  const [imcValues, setImcValues] = useState<number[]>([]); // Tableau de valeurs d'IMC (numériques)
  // Extraction des dates et des valeurs d'IMC pour le patient

// Configuration des données pour le graphique
const data = {
  labels: imcDates,
  datasets: [
    {
      label: 'IMC du patient',
      data: imcValues,
      fill: false,
      borderColor: 'rgb(75, 192, 192)',
      tension: 0.1,
    },
  ],
};
const options = {
  responsive: true,
  plugins: {
    legend: {
      position: 'top' as const,
    },
    title: {
      display: true,
      text: 'Courbe imc du patient',
    },
  },
};

const [poids, setpoids] = useState([]);
  const [poidDates, setPoidDates] = useState<string[]>([]); // Tableau de dates (chaînes)
  const [poidValues, setPoidValues] = useState<number[]>([]); // Tableau de valeurs d'IMC (numériques)
  // Extraction des dates et des valeurs d'IMC pour le patient

// Configuration des données pour le graphique
const data1 = {
  labels: poidDates,
  datasets: [
    {
      label: 'Poids du patient',
      data: poidValues,
      fill: false,
      borderColor: 'rgb(75, 192, 192)',
      tension: 0.1,
    },
  ],
};
const options1 = {
  responsive: true,
  plugins: {
    legend: {
      position: 'top' as const,
    },
    title: {
      display: true,
      text: 'Courbe poids du patient',
    },
  },
};

const [epa, setepas] = useState([]);
const [epaDates, setEpaDates] = useState<string[]>([]); // Tableau de dates (chaînes)
const [epaValues, setEpaValues] = useState<number[]>([]); // Tableau de valeurs d'IMC (numériques)
// Extraction des dates et des valeurs d'IMC pour le patient

// Configuration des données pour le graphique
const data2 = {
  labels: epaDates,
  datasets: [
    {
      label: 'EPA du patient',
      data: epaValues,
      fill: false,
      borderColor: 'rgb(75, 192, 192)',
      tension: 0.1,
    },
  ],
};
const options2 = {
  responsive: true,
  plugins: {
    legend: {
      position: 'top' as const,
    },
    title: {
      display: true,
      text: 'Courbe epa du patient',
    },
  },
};

const getCardColorClass = (status) => {
  switch (status) {
    case 'dénutrition avérée':
      return 'info-card-red';
    case 'surveillance':
      return 'info-card-orange';
    case 'normal':
      return 'info-card-blue';
    default:
      return '';
  }
};

  const patientEntity = useAppSelector(state => state.patient.entity);
  return (
    <Row className="container">
      <div className="container_info">
        <div className={`patient-card ${getCardColorClass(patientEntity.statut)}`}>
          <div>
            <img src="../content/images/logo.jpeg" alt="Photo du patient" className="photo_patient"/>
          </div>
          <div className="info_patient_administratives">
            <div>
              <div>
                <span id="prenom">
                  <Translate contentKey="ecomApp.patient.prenom"></Translate>{patientEntity.prenom}
                </span>
              </div>
              <div>
                <span id="nom">
                  <Translate contentKey="ecomApp.patient.nom"></Translate>{patientEntity.nom}
                </span>
              </div>
              <div>
                <span id="dateNaissance">
                  <Translate contentKey="ecomApp.patient.dateNaissance"></Translate>{patientEntity.dateNaissance ? <TextFormat value={patientEntity.dateNaissance} type="date" format={APP_DATE_FORMAT} /> : null}
                </span>
              </div>
            </div>
            <div>
              <div>
                <span id="datearrive">
                  <Translate contentKey="ecomApp.patient.datearrive"></Translate>{patientEntity.datearrive ? <TextFormat value={patientEntity.datearrive} type="date" format={APP_DATE_FORMAT} /> : null}
                </span>
              </div>
              <div>
                <button>
                  Statut
                </button>
              </div>
            </div>
          </div>
        </div>
        <div className="info_patient_perso">
          <div>
            <div>
              <span id="taille">
                <Translate contentKey="ecomApp.patient.taille"></Translate>{patientEntity.taille} cm
              </span>
            </div>
            <div>
              <span id="poids">
                <Translate contentKey="ecomApp.patient.poids"></Translate>{patientEntity.poids} kg
              </span>
            </div>
          </div>
          <div>
            <div>
              <span id="ePA">
                <Translate contentKey="ecomApp.patient.ePA"></Translate>{patientEntity.ePA}
              </span>
            </div>
            <div>
              <span id="iMC">
                <Translate contentKey="ecomApp.patient.iMC"></Translate>{patientEntity.iMC}
              </span>
            </div>
            <div>
              <span id="albumine">
                <Translate contentKey="ecomApp.patient.albumine"></Translate>{patientEntity.albumine ? patientEntity.albumine.id : ''}
              </span>
            </div>
          </div>
        </div>
        <div className="info_buttons">
          <button onClick={null}>
            Données administratives
          </button>
          <button onClick={null}>
            Données patient
          </button>
          <button onClick={null}>
            Tâche
          </button>
        </div>
      </div>
      <div className="menu_patient">
        <nav>
          <ul>
            <li>
              <Link to="">Courbes</Link>
            </li>
            <li>
              <Link to="/note">Notes</Link>
            </li>
            <li>
              <Link to="/rappel">Tâches</Link>
            </li>
            <li>
              <Link to="/alerte">Alertes</Link>
            </li>
          </ul>
        </nav>
      </div>
      <Col md="8">
       <Line options={options} data={data} />
       <Line options={options1} data={data1} />
        <Line options={options2} data={data2} />
       </Col>
      <Col md="9">
      <Button tag={Link} to="/" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/patient/${patientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Visualiser données</Translate>
          </span>
        </Button>
      </Col>

    </Row>
    
  );
};

export default Patient;
