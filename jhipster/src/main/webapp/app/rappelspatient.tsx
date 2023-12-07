
import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getRappelsByPatient ,toggleVerif} from 'app/entities/rappel/rappel.reducer';
import { useParams } from 'react-router-dom';
import './shared/layout/menus/Rappel.css'; // Import your CSS file
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCalendar } from '@fortawesome/free-solid-svg-icons';


export const RappelPatient = ({ idprops }: { idprops: string }) => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();
  const [filter, setFilter] = useState('all'); 
  const [selectedDate, setSelectedDate] = useState(null); 
  const updateSuccess = useAppSelector(state => state.rappel.updateSuccess);

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
}, [account.login, dispatch,updateSuccess]);

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
    const rappelDate = new Date(rappel.date);
    const today = new Date();
  
    rappelDate.setHours(0, 0, 0, 0);
    today.setHours(0, 0, 0, 0);
  
    const isFuture = rappelDate.getTime() > today.getTime();
  
    if (filter === 'all' && (!selectedDate || rappelDate.toDateString() === selectedDate.toDateString()) && !isFuture) return true;
    if (filter === 'verified' && rappel.verif && (!selectedDate || rappelDate.toDateString() === selectedDate.toDateString()) && !isFuture) return true;
    if (filter === 'unverified' && !rappel.verif && (!selectedDate || rappelDate.toDateString() === selectedDate.toDateString()) && !isFuture) return true;
    if (filter === 'futur' && isFuture) return true; 
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
        <option value="futur">Rappels futurs</option>
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
      {filteredRappels.map(rappel => (
        <div key={rappel.id} className="rappel">
          <div className="rappel-content">
          <div className="rappel-icon">
        {rappel.action === 'Regarder le dossier' ? '📁' : rappel.action === 'prise de poids' ? '⚖️' : '⚠️'}
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