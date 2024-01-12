import React from 'react';

const Consent = () => {
  return (
    <div>
      <div>
        <h1>Formulaire de consentement</h1>

        <h2>Données Personnelles Collectées :</h2>
        <ul>
          <li>Nom</li>
          <li>Prénom</li>
          <li>Etablissement de travail</li>
          <li>Poste occupé</li>
          <li>Login</li>
          <li>Mot de passe</li>
          <li>Email</li>
          <li>Adresse IP professionnelle</li>
        </ul>

        <h2>Finalités du Traitement :</h2>
        <ul>
          <li>Fourniture de services personnalisés</li>
          <li>Communication relative aux mises à jour</li>
          <li>Analyse et amélioration des services</li>
        </ul>

        <h2>Partage d'Informations :</h2>
        <ul>
          <li>Avec mon consentement explicite.</li>
          <li>Conformément à la loi ou en réponse à une demande d'une autorité gouvernementale.</li>
        </ul>

        <h2>Durée de Conservation :</h2>
        <p>Mes données seront conservées aussi longtemps que nécessaire pour atteindre les finalités mentionnées ci-dessus.</p>

        <h2>Droits de l'Utilisateur :</h2>
        <p>J'ai le droit de demander l'accès, la correction ou la suppression de mes données personnelles. Je peux exercer ces droits en contactant NutriPro à NutriPro2024@gmail.com.</p>

        <h2>Consentement :</h2>
        <p>Je consens expressément au traitement de mes données personnelles tel que décrit dans ce formulaire.</p>
        
        <h2>Révocation du Consentement :</h2>
        <p>Je comprends que je peux révoquer ce consentement à tout moment en contactant NutriPro à NutriPro2024@gmail.com.</p>

        <p>En acceptant ce formulaire, je reconnais avoir lu et compris les termes de cette déclaration de consentement et consens au traitement de mes données personnelles par NutriPro.</p>
      </div>
    </div>
  );
};

export default Consent;
