import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getRappelsByUser, toggleVerif } from 'app/entities/rappel/rappel.reducer';
import { Link } from 'react-router-dom';
import './Rappel.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFileAlt, faCalendar } from '@fortawesome/free-solid-svg-icons';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
export const Rappels = () => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();
  const [filter, setFilter] = useState('all');

  const [rappels, setRappels] = useState([]);
  useEffect(() => {
    if (account && account.login) {
      dispatch(getRappelsByUser(account.login))
      .then(response => {
        setRappels((response.payload as any).data);
      })
      .catch(error => {
        console.error('Une erreur s\'est produite :', error);
      });
    }
  }, [account.login, dispatch]);
  const [selectedDate, setSelectedDate] = useState(null);

  const handleToggleVerif = (id: string | number) => { // New function to handle click
    dispatch(toggleVerif(id))
      .then(() => {
        // Refresh the alertes after toggling verif
        if (account && account.login) {
          dispatch(getRappelsByUser(account.login))
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
    if (filter === 'verified' && rappel.verif && (!selectedDate || rappelDate.toDateString() === selectedDate.toDateString())) return true;
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

  const [currentRappel, setCurrentRappel] = useState(null);
  return (
    <div style={{ minHeight: '1000px'}}>
      <div>
      <select style={{marginRight: '5px'}} onChange={(e) => setFilter(e.target.value)}>
        <option value="all">Tout les rappels</option>
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
    <div className="rappel-icon">
    ⚠️
    </div>
    <div style={{ display: 'flex'}}>
      <span className="category">Patient : </span> <span style={{fontSize: '1.2em'}}>{rappel.patient.nom} {rappel.patient.prenom}</span>
    </div>
    <div style={{ display: 'flex'}}>
      <span className="category">Tâches : </span> <span style={{fontSize: '1.2em'}}> {rappel.action}</span>
    </div>
    <div style={{ display: 'flex'}}>
      <span className="category">Date : </span> <span style={{fontSize: '1.2em'}}> {new Date(rappel.date).toLocaleDateString()}</span>
    </div>
    <div style={{ display: 'flex'}}>
      {rappel.patient && <Link to={`/patients/${rappel.patient.id}`} className="rappel-button">
        <FontAwesomeIcon icon={faFileAlt} />
        Voir patient
      </Link>}
      <button className="rappel-check" onClick={() => handleToggleVerif(rappel.id)}>
        {rappel.verif ? '✅' : '⬜'}
      </button>
    </div>
  </div>
))}
    </div>
  );
};

export default Rappels;