
import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getRappelsByPatient ,toggleVerif} from 'app/entities/rappel/rappel.reducer';
import { useParams } from 'react-router-dom';
import './shared/layout/menus/Rappel.css'; // Import your CSS file
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCalendar } from '@fortawesome/free-solid-svg-icons';
import CreationRappel from './shared/layout/menus/creationRappel';
import Modal from 'react-modal';
import { Button } from 'reactstrap';

export const RappelPatient = ({ idprops }: { idprops: string }) => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();
  const [filter, setFilter] = useState('all'); 
  const [selectedDate, setSelectedDate] = useState(null); 
  const [modal, setModal] = useState(false);

  const toggle = () => setModal(!modal);
  const [rappels, setRappels] = useState([]);
useEffect(() => {
    if (account && account.login) {
        dispatch(getRappelsByPatient(idprops))
            .then(response => {
                setRappels((response.payload as any).data);
                console.log(rappels);
            })
            .catch(error => {
                console.error('Une erreur s\'est produite :', error);
            });
    }
}, [account.login, dispatch]);

const handleToggleVerif = (alertId: string | number) => { 
    dispatch(toggleVerif(alertId))
    .then(() => {
      if (account && account.login) {
        dispatch(getRappelsByPatient(idprops))
        .then(response => {
            setRappels((response.payload as any).data);
        });
      }
    });
  };

  const filteredRappels = rappels.filter(rappel => {
    if (filter === 'all' && (!selectedDate || new Date(rappel.date).toDateString() === selectedDate.toDateString())) return true;
    if (filter === 'verified' && rappel.verif && (!selectedDate || new Date(rappel.date).toDateString() === selectedDate.toDateString())) return true;
    if (filter === 'unverified' && !rappel.verif && (!selectedDate || new Date(rappel.date).toDateString() === selectedDate.toDateString())) return true;
    return false;
  });

  const [showDatePicker, setShowDatePicker] = useState(false);

  const handleCalendarIconClick = () => {
    setShowDatePicker(!showDatePicker);
  };

  const handleDatePickerClose = () => {
    setShowDatePicker(false);
  };
  
return (
    <div>
      <div>
      <select onChange={(e) => setFilter(e.target.value)}>
        <option value="all">Tout les Rappels</option>
        <option value="verified">Rappels vérifiées</option>
        <option value="unverified">Rappels non vérifiées</option>
      </select>
      <FontAwesomeIcon icon={faCalendar} onClick={handleCalendarIconClick} />
        {showDatePicker && (
          <DatePicker
            selected={selectedDate}
            onChange={date => setSelectedDate(date)}
            placeholderText='Sélectionnez une date'
            isClearable
          />
        )}    
         </div>
         <Button color="danger" onClick={toggle}>Create Rappel</Button>
      <CreationRappel modal={modal} toggle={toggle} idprops={idprops}/>
      {filteredRappels.map(rappel => (
        <div key={rappel.id} className="rappel">
          <div className="rappel-content">
          <div className="rappel-icon">
        {rappel.action === 'surveillance' ? '📁' : rappel.action === 'prise de poids' ? '⚖️' : '⚠️'}
      </div>
            <div>
              <p>Tâche: {rappel.action}</p>
              <p>Date: {rappel.date}</p>
            </div>
            <button className="rappel-check" onClick={() => handleToggleVerif(rappel.id)}>
            {rappel.verif ? '✅' : '⬜'}
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default RappelPatient;