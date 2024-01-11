import React, {useState, useEffect} from 'react';
import {useAppDispatch, useAppSelector} from 'app/config/store';
import {getAlertesByPatientAndUser, toggleVerif} from 'app/entities/alerte/alerte.reducer';
import {useParams} from 'react-router-dom';
import './shared/layout/menus/Alerte.css';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faCalendar} from '@fortawesome/free-solid-svg-icons';

export const AlertePatient = ({idprops}: { idprops: string }) => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();
  const {id} = useParams<'id'>();
  const [filter, setFilter] = useState('all');
  const [selectedDate, setSelectedDate] = useState(null);

  const [alertes, setAlertes] = useState([]);
  useEffect(() => {
    if (account && account.login) {
      dispatch(getAlertesByPatientAndUser({id: idprops, login: account.login}))
        .then(response => {
          setAlertes((response.payload as any).data);
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
          dispatch(getAlertesByPatientAndUser({id: idprops, login: account.login}))
            .then(response => {
              setAlertes((response.payload as any).data);
            });
        }
      });
  };

  const filteredAlertes = alertes.filter(alerte => {
    if (filter === 'all' && (!selectedDate || new Date(alerte.date).toDateString() === selectedDate.toDateString())) return true;
    if (filter === 'verified' && alerte.verif && (!selectedDate || new Date(alerte.date).toDateString() === selectedDate.toDateString())) return true;
    if (filter === 'unverified' && !alerte.verif && (!selectedDate || new Date(alerte.date).toDateString() === selectedDate.toDateString())) return true;
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
        <select style={{marginRight: '5px'}} onChange={(e) => setFilter(e.target.value)}>
          <option value="all">Toutes les alertes</option>
          <option value="verified">Alertes vérifiées</option>
          <option value="unverified">Alertes non vérifiées</option>
        </select>
        <FontAwesomeIcon icon={faCalendar} onClick={handleCalendarIconClick}/>
        {showDatePicker && (
          <DatePicker
            selected={selectedDate}
            onChange={date => setSelectedDate(date)}
            placeholderText='Sélectionnez une date'
            isClearable
          />
        )}
      </div>
      {filteredAlertes.map(alerte => (
        <div key={alerte.id} className="alerte">
          <div className="alerte-icon">⚠️</div>
          <div style={{ display: 'flex'}}>
              <span className="category">Patient : </span> <span style={{fontSize: '1.2em'}}>{alerte.patient.nom} {alerte.patient.prenom}</span>
          </div>
          <div style={{ display: 'flex'}}>
            <span className="category">Action : </span> <span style={{fontSize: '1.2em'}}>{alerte.action}</span>
          </div>
          <div style={{ display: 'flex'}}>
            <span className="category">Date : </span> <span style={{fontSize: '1.2em'}}>{new Date(alerte.date).toLocaleDateString()}</span>
          </div>
          <div style={{ display: 'flex'}}>
            <button className="alerte-check" onClick={() => handleToggleVerif(alerte.id)}>
              {alerte.verif ? '✅' : '⬜'}
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default AlertePatient;
