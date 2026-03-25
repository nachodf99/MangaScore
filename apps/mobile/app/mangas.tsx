import { Text, View } from 'react-native';

export default function MangasScreen() {
  return (
    <View style={{ flex: 1, padding: 20, backgroundColor: '#020617' }}>
      <Text style={{ color: '#FFF', fontSize: 22, fontWeight: '700' }}>Mangas activas</Text>
      <Text style={{ color: '#94A3B8', marginTop: 10 }}>Aquí se mostrará cache local de manga + participantes.</Text>
    </View>
  );
}
