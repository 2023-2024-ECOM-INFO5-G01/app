import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity, updateStatus } from './entities/patient/patient.reducer';

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

  const patientEntity = useAppSelector(state => state.patient.entity);
  
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

  const getCardColorClass = (status) => {
    switch (status) {
      case 'dénutrition avérée':
        return 'info-card red';
      case 'surveillance':
        return 'info-card orange';
      case 'normal':
        return 'info-card blue';
      default:
        return '';
    }
  };

  // Options de statut disponibles
  const optionsStatut = ['normal', 'surveillance', 'dénutrition avérée'];

  // Gestionnaire d'événements pour la modification du statut
  const handleStatutChange = (event) => {
    const nouveauStatutSelectionne = event.target.value;
    // changement de statut avec le nouveau statut
    dispatch(updateStatus({ id: id, statut: nouveauStatutSelectionne }));
    // Actualiser la page
    window.location.reload();
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
        return <div>Alerte content goes here</div>;
      default:
        return <Col md="12" className={`graphs ${isFixed ? 'fixed' : 'relative'}`}>
        <Line options={options} data={data} />
        <Line options={options1} data={data1} />
         <Line options={options2} data={data2} />
        </Col>;
    }
  };

  
  return (
    <Row className="container-fluid">
      <div className={`sticky-div ${isFixed ? 'fixed' : 'relative'}`}>
        <Col className="fixed-flex-container">
          <div className={`patient-card ${getCardColorClass(patientEntity.statut)}`}>
            <div className="flex_div">
              <img src="../content/images/logo.jpeg" alt="Photo du patient" className="photo_patient"/>
              <button onClick={togglePosition} className={`button_${isFixed ? 'release' : 'clicked'}`}>
                <img src="../content/images/pin.svg" alt="Punaise" className="img_patient_pin"/>
              </button>
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
                  <label>Statut : </label>
                  <select value={patientEntity.statut} onChange={handleStatutChange}>
                    {optionsStatut.map((optionStatut) => (
                      <option key={optionStatut} value={optionStatut}>
                        {optionStatut}
                      </option>
                    ))}
                  </select>
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
              <button className="bouton_modif_patient" onClick={null}>
                <img src="../content/images/icons8-plus-50.png" alt="Icon svg plus" className="img_patient_plus"/>
                Données administratives
              </button>
              <button className="bouton_modif_patient" onClick={null}>
                <img src="../content/images/icons8-plus-50.png" alt="Icon svg plus" className="img_patient_plus"/>
                Données patient
              </button>
              <button className="bouton_modif_patient" onClick={null}>
                <img src="../content/images/icons8-plus-50.png" alt="Icon svg plus" className="img_patient_plus"/>
                Tâche
              </button>
            </div>
          </Col>
          <div className="menu_patient">
            <nav>
              <ul>
                <li>
                  <button className="bouton_menu_patient" onClick={() => changeTab('Courbes')}>Courbes</button>
                </li>
                <li>
                  <button className="bouton_menu_patient" onClick={() => changeTab('note')}>Notes</button>
                </li>
                <li>
                  <button className="bouton_menu_patient" onClick={() => changeTab('rappel')}>Tâches</button>
                </li>
                <li>
                  <button className="bouton_menu_patient" onClick={() => changeTab('alerte')}>Alertes</button>
                </li>
              </ul>
            </nav>
          </div>
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
