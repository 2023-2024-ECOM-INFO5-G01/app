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
import AlertePatient from './alertespatient';

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
    console.log("entity",patientEntity );
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

//code pour le bouton toogle fixed
const [isFixed, setIsFixed] = useState(false);

const togglePosition = () => {
  setIsFixed((prevIsFixed) => !prevIsFixed);
};

const [activeTab, setActiveTab] = useState('accueil');

  const changeTab = (tabName) => {
    setActiveTab(tabName);
  };

  const renderTabContent = () => {
    switch (activeTab) {
      case 'note':
        return <div>Notes content goes here</div>;
      case 'rappel':
        return <div>Rappel content goes here</div>;
      case 'alerte':
        return <div><AlertePatient idprops={id}/></div>;
      default:
        return <div><Col md="12" className={`graphs ${isFixed ? 'fixed' : 'relative'}`}>
        <Line options={options} data={data} />
        <Line options={options1} data={data1} />
         <Line options={options2} data={data2} />
        </Col></div>;
    }
  };

  const patientEntity = useAppSelector(state => state.patient.entity);
  return (
    <Row className="container-fluid">
      <div className={`sticky-div ${isFixed ? 'fixed' : 'relative'}`}>
        <button onClick={togglePosition}>
          Position ({isFixed ? 'fixed' : 'relative'})
        </button>
        <div>
        <Col md="8" className="fixed-flex-container" xs="12">
            <div>
              <h2 data-cy="patientDetailsHeading">
                <Translate contentKey="ecomApp.patient.detail.title">Patient</Translate>
              </h2>
              <img src="../content/images/logo.jpeg" alt="Photo du patient" className="logo"/>
            </div>
            <dl className="jh-entity-details">
              <div>
                <dt>
                  <span id="id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </span>
                </dt>
                <dd>{patientEntity.id}</dd>
              </div>
              <div>
                <dt>
                  <span id="nom">
                    <Translate contentKey="ecomApp.patient.nom">Nom</Translate>
                  </span>
                </dt>
                <dd>{patientEntity.nom}</dd>
              </div>
              <div>
                <dt>
                  <span id="prenom">
                    <Translate contentKey="ecomApp.patient.prenom">Prenom</Translate>
                  </span>
                </dt>
                <dd>{patientEntity.prenom}</dd>
              </div>
              <div>
                <dt>
                  <span id="statut">
                    <Translate contentKey="ecomApp.patient.statut">Statut</Translate>
                  </span>
                </dt>
                <dd>{patientEntity.statut}</dd>
              </div>
              <div>
                <dt>
                  <span id="dateNaissance">
                    <Translate contentKey="ecomApp.patient.dateNaissance">Date Naissance</Translate>
                  </span>
                </dt>
                <dd>
                  {patientEntity.dateNaissance ? <TextFormat value={patientEntity.dateNaissance} type="date" format={APP_DATE_FORMAT} /> : null}
                </dd>
              </div>
              <div>
                <dt>
                  <span id="taille">
                    <Translate contentKey="ecomApp.patient.taille">Taille</Translate>
                  </span>
                </dt>
                <dd>{patientEntity.taille}</dd>
              </div>
              <div>
                <dt>
                  <span id="datearrive">
                    <Translate contentKey="ecomApp.patient.datearrive">Datearrive</Translate>
                  </span>
                </dt>
                <dd>{patientEntity.datearrive ? <TextFormat value={patientEntity.datearrive} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
              </div>
              <div>
                <dt>
                  <Translate contentKey="ecomApp.patient.albumine">Albumine</Translate>
                </dt>
                <dd>{patientEntity.albumine ? patientEntity.albumine.id : ''}</dd>
              </div>
              <div>
                <dt>
                  <Translate contentKey="ecomApp.patient.ehpad">Ehpad</Translate>
                </dt>
                <dd>{patientEntity.ehpad ? patientEntity.ehpad.id : ''}</dd>
              </div>
              <div>
                <dt>
                  <Translate contentKey="ecomApp.patient.user">User</Translate>
                </dt>
                <dd>
                  {patientEntity.users
                    ? patientEntity.users.map((val, i) => (
                        <span key={val.id}>
                          <a>{val.id}</a>
                          {patientEntity.users && i === patientEntity.users.length - 1 ? '' : ', '}
                        </span>
                      ))
                    : null}
                </dd>
              </div>
            </dl>
          </Col>
        </div>
        <nav>
          <ul>
            <li>
            <button onClick={() => changeTab('Courbes')}>Courbes</button>
            </li>
            <li>
            <button onClick={() => changeTab('note')}>Notes</button>
            </li>
            <li>
            <button onClick={() => changeTab('rappel')}>Tâches</button>
            </li>
            <li>
            <button onClick={() => changeTab('alerte')}>Alertes</button>
            </li>
          </ul>
        </nav>
      </div>
      {renderTabContent()}
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
