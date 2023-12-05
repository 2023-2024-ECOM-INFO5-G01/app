import React, {useEffect, useState} from 'react';
import {Link, useParams} from 'react-router-dom';
import {Button, Row, Col} from 'reactstrap';
import {Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {useAppDispatch, useAppSelector} from 'app/config/store';

import {getEntity} from '../entities/patient/patient.reducer';
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

import '../../content/css/patient.css';
import AlertePatient from '../alertespatient';
import {GraphTab} from "app/PatientView/graph_tab";
import {PatientTabs} from "app/PatientView/patient_tabs";
import {PatientThumbnail} from "app/PatientView/patient_thumbnail";

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

  const {id} = useParams<'id'>();
  const [statuschange, setStatuschange] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setStatuschange(false);
  }, [statuschange]);

  //code pour le bouton toogle fixed
  const [isFixed, setIsFixed] = useState(false);

  const togglePosition = () => {
    setIsFixed((prevIsFixed) => !prevIsFixed);
  };

  const [activeTab, setActiveTab] = useState('accueil');

  const renderTabContent = () => {
    switch (activeTab) {
      case 'note':
        return <div>Notes content goes here</div>;
      case 'rappel':
        return <div>Rappel content goes here</div>;
      case 'alerte':
        return <div><AlertePatient idprops={id}/></div>;
      default:
        return <GraphTab isFixed={isFixed}/>;
    }
  };

  const patientEntity = useAppSelector(state => state.patient.entity);

  return (
    <Row className="container-fluid">
      <div className={`sticky-div ${isFixed ? 'fixed' : 'relative'}`}>
        <PatientThumbnail togglePosition={togglePosition} isFixed={isFixed} patientEntity={patientEntity}
                          setStatus={setStatuschange}/>
        <PatientTabs changeTab={setActiveTab}/>
      </div>
      <div className={`graphs ${isFixed ? 'fixed' : 'relative'}`}>
        {renderTabContent()}
      </div>
      <Col md="9">
        <Button tag={Link} to="/" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left"/>{' '}
          <span className="d-none d-md-inline">
             <Translate contentKey="entity.action.back">Back</Translate>
           </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/patient/${patientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt"/>{' '}
          <span className="d-none d-md-inline">
             <Translate contentKey="entity.action.edit">Visualiser données</Translate>
           </span>
        </Button>
      </Col>
    </Row>
  );
};

export default Patient;