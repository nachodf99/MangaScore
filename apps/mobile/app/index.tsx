import { Link } from 'expo-router';
import { Text, View } from 'react-native';

export default function HomeScreen() {
  return (
    <View style={{ flex: 1, padding: 20, gap: 16, backgroundColor: '#0F172A' }}>
      <Text style={{ color: '#FFF', fontSize: 26, fontWeight: '700' }}>MangaFish</Text>
      <Text style={{ color: '#E2E8F0', fontSize: 16 }}>Modo offline-first listo para jueces.</Text>
      <Link href="/mangas" style={{ color: '#38BDF8', fontSize: 20 }}>
        Ir a mangas activas
      </Link>
      <Link href="/sync" style={{ color: '#38BDF8', fontSize: 20 }}>
        Ver cola de sincronización
      </Link>
    </View>
  );
}
