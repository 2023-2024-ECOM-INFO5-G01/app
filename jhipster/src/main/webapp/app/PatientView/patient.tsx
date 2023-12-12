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
  Legend
} from 'chart.js';
import RappelPatient from 'app/rappelspatient';
import '../../content/css/patient.css';
import AlertePatient from '../alertespatient';
import {GraphTab} from "app/PatientView/graph_tab";
import {PatientTabs} from "app/PatientView/patient_tabs";
import {PatientThumbnail} from "app/PatientView/patient_thumbnail";
import Notes from "app/note";
import {IPatient} from "app/shared/model/patient.model";


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
  const [background, setbackground] = useState(null);

  useEffect(() => {
    dispatch(getEntity(id));
    setStatuschange(false);
  }, [statuschange]);

  //code pour le bouton toogle fixed
  const [isFixed, setIsFixed] = useState(false);

  const togglePosition = () => {
    setIsFixed((prevIsFixed) => !prevIsFixed);
  };
  const account = useAppSelector(state => state.authentication.account);
  const userHasRequiredRole = account.authorities.some(role => ['ROLE_MEDECIN', 'ROLE_ADMIN'].includes(role));

  const [activeTab, setActiveTab] = useState(userHasRequiredRole ? 'accueil' : 'rappel');

  const renderTabContent = () => {
    switch (activeTab) {
      case 'note':
        return userHasRequiredRole && <div><Notes idprops={id}/></div>;
      case 'rappel':
        return <div><RappelPatient idprops={id}/></div>;
      case 'alerte':
        return userHasRequiredRole && <div><AlertePatient idprops={id}/></div>;
      default:
        return userHasRequiredRole && <GraphTab isFixed={isFixed}/>;
    }
  };

  const patientEntity : IPatient = useAppSelector(state => state.patient.entity);

  return (
    <div>
      <Row className="container-fluid">
        <div className={`sticky-div ${isFixed ? 'fixed' : 'relative'}`}>
          <PatientThumbnail togglePosition={togglePosition} isFixed={isFixed} patientEntity={patientEntity}
                            setStatus={setStatuschange} idprops={id} setbackground={setbackground} />
          <PatientTabs changeTab={setActiveTab} />
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
        </Col>
      </Row>
      {background && <div className='background' onClick={() => setbackground(null)}>{background}</div>}
    </div>
  );
};

export default Patient;
