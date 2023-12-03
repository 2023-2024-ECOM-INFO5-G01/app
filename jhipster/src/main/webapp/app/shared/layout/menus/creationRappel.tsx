import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from '../../util/date-utils'
import { mapIdList } from '../../util/entity-utils';
import { useAppDispatch, useAppSelector } from '../../../config/store';

import { IUser } from '../../model/user.model';
import { getUsers } from '../../../modules/administration/user-management/user-management.reducer';
import { IPatient } from '../../model/patient.model';
import { getEntities as getPatients } from '../../../entities/patient/patient.reducer';
import { IRappel } from '../../model/rappel.model';
import { getEntity, updateEntity, createEntity, reset } from '../../../entities/rappel/rappel.reducer';
export const CreationRappel = ({ modal, toggle ,idprops}: { modal: boolean; toggle: () => void; idprops: string }) => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const patients = useAppSelector(state => state.patient.entities);
  const rappelEntity = useAppSelector(state => state.rappel.entity);
  const loading = useAppSelector(state => state.rappel.loading);
  const updating = useAppSelector(state => state.rappel.updating);
  const updateSuccess = useAppSelector(state => state.rappel.updateSuccess);
  const [selectedAction, setSelectedAction] = useState('Regarder le dossier');
  const handleClose = () => {
    navigate(`/patients/${idprops}`);
  };

  useEffect(() => {
      dispatch(reset());
    dispatch(getUsers({}));
    dispatch(getPatients({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
      toggle();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    
  
    const entity = {
      ...rappelEntity,
      ...values,
      verif: false,
      user: users.find(it => it.id.toString() === values.user.toString()),
      patient: patients.find(it => it.id.toString() === idprops),
    };
  
    if (selectedAction === 'prise de poids') {
      const startDate = new Date();
      const endDate =values.endDate ? convertDateTimeToServer(values.endDate) : null;
      console.log("date : " + startDate+ ""+ "endDate:" + endDate + "period:" + values.period + "frequence:" + values.frequency );
      if (!startDate || !endDate || startDate >= endDate) {
        alert("La date de fin doit être postérieure à la date de début");
        return;
      }
      if (startDate.getTime() < endDate.getTime()) {
        console.log('entity:', entity, 'currentDate:', startDate);
        const dates = addPeriod(startDate, values.period, values.frequency, endDate);
        console.log('dates:', dates);
        for (let date of dates) {
          const newEntity = {
            ...entity,
            date: date,
          };
          dispatch(createEntity(newEntity));
        }
      }
    } else {
      values.date = convertDateTimeToServer(values.date);
      console.log('values.date:', values.date);
      const newEntity = {
        ...entity,
        date: values.date,
      };
      dispatch(createEntity(newEntity));
    }
  };
  
  
  
  function addPeriod(date, period, frequency, endDate) {
    let newDate = new Date(date);
    const datetab = [] ;
    switch (period) {
      case 'day':
        let interval = Math.round(24 / frequency); // Divide the day into equal intervals
        let reminderDate = new Date(newDate);
        //ajouter un jour à reminderDate
        reminderDate.setDate(reminderDate.getDate() + 1);
        reminderDate.setHours(0); // Set the hour to the first hour of the day

        while (reminderDate.getTime() < endDate.getTime()) {
        for (let i = 0; i < frequency; i++) {
        // Create a new date object with the same date but different time
            reminderDate.setHours(i * interval); // Set the hour to the current interval

          // Check if the reminder date is still within the same day
            if ( reminderDate.getTime() < endDate.getTime()) {
              datetab.push(new Date(reminderDate));
              console.log("date add : " + reminderDate);
              console.log("datetab add : " + datetab);
          }
          }
          reminderDate.setHours(0); // Reset the hours to zero
          reminderDate.setDate(reminderDate.getDate() + 1);
          console.log("ajoute un jour:" + "reminderdate" + reminderDate + "endDate" + endDate);

        }
        break;
        case 'month':
  let reminderDate2 = new Date(newDate);
        let monthInterval = Math.round(30 / frequency); // Divide the month into equal intervals
  while (reminderDate2.getTime() < endDate.getTime()) {
    for (let i = 0; i < frequency; i++) {
      // Save the current month
      let currentMonth = reminderDate2.getMonth();
      // Set the day of the month to i + 1
      reminderDate2.setDate(i * monthInterval + 1);

      // Check if the month has changed
      if (reminderDate2.getMonth() !== currentMonth) {
        // The day was too big for the month, so reset it to the last day of the previous month
        reminderDate2.setDate(0);
      }

      // Check if the reminder date is still within the same month
      if (reminderDate2.getTime() < endDate.getTime() && reminderDate2.getTime() > date.getTime()) {
        datetab.push(new Date(reminderDate2));
      }
    }

    // Increment the month of the reminder date
    reminderDate2.setMonth(reminderDate2.getMonth() + 1);


  }
  break;
        
      case 'year':
        let yearInterval = Math.round(12 / frequency); // Divide the year into equal intervals
        let reminderDate3 = new Date(newDate);
      while (reminderDate3.getTime() < endDate.getTime()) {
        for (let i = 0; i < frequency; i++) {
          // Create a new date object with the same date but different time
          reminderDate3.setMonth(i * yearInterval);

          // Check if the reminder date is still within the same year
          if ( reminderDate3.getTime() < endDate.getTime() && reminderDate3.getTime() > date.getTime()) {
            datetab.push(new Date(reminderDate3));
          }
        }
        reminderDate3.setMonth(1); // Reset the month to the first month of the year
        reminderDate3.setFullYear(reminderDate3.getFullYear() + 1);
      }
        break;
    }
    return datetab;
  }

  const defaultValues = () =>
    isNew
      ? {
          date: displayDefaultDateTime(),
        }
      : {
          ...rappelEntity,
          date: convertDateTimeFromServer(rappelEntity.date),
          user: rappelEntity?.user?.id,
          patient: rappelEntity?.patient?.id,
        };

  return (
    <Modal isOpen={modal} toggle={toggle}>
    <ModalHeader toggle={toggle}>Create Rappel</ModalHeader>
    <ModalBody>
      <Row className="justify-content-center">
        <Col md="8">
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
<ValidatedField
  id="rappel-action"
  name="action"
  data-cy="action"
  label={translate('ecomApp.rappel.action')}
  type="select"
  onChange={e => setSelectedAction(e.target.value)}
>
  <option value="Regarder le dossier">Regarder le dossier</option>
  <option value="prise de poids">Prise de poids</option>
  </ValidatedField>

  <ValidatedField id="rappel-user" name="user" data-cy="user" label={translate('ecomApp.rappel.user')} type="select" >
                      <option value="" key="0" />
                      {users
                        ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                        : null}
                    </ValidatedField>
                    <ValidatedField
                        label={translate('ecomApp.rappel.date')}
                        id="rappel-date"
                        name="date"
                        data-cy="date"
                        type="datetime-local"
                        style={{ display: selectedAction === 'Regarder le dossier' ? 'block' : 'none' }}
                        validate={{
                        required: selectedAction === 'Regarder le dossier' ? 'Ce champ est obligatoire' : false}}
                        placeholder="YYYY-MM-DD HH:mm" />
    <ValidatedField
      id="rappel-frequency"
      name="frequency"
      data-cy="frequency"
      label="Fréquence"
      type="number"
      style={{ display: selectedAction === 'prise de poids' ? 'block' : 'none' }}
      validate={{ required: selectedAction === 'prise de poids' ? 'Ce champ est obligatoire' : false }}
    />
    <ValidatedField
      id="rappel-period"
      name="period"
      data-cy="period"
      label="Période"
      type="select"
      style={{ display: selectedAction === 'prise de poids' ? 'block' : 'none' }}
      validate={{ required: selectedAction === 'prise de poids' ? 'Ce champ est obligatoire' : false }}
    >
      <option value="day">Jour</option>
      <option value="month">Mois</option>
      <option value="year">Année</option>
    </ValidatedField>
    <ValidatedField
      id="rappel-endDate"
      name="endDate"
      data-cy="endDate"
      label="Date de fin"
      type="datetime-local"
      style={{ display: selectedAction === 'prise de poids' ? 'block' : 'none' }}
      validate={{ required: selectedAction === 'prise de poids' ? 'Ce champ est obligatoire' : false }}
      placeholder="YYYY-MM-DD HH:mm" />
   

<Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
        </Col>
      </Row>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={toggle}>Cancel</Button>
      </ModalFooter>
    </Modal>
  );
};

export default CreationRappel;
