import apiClient from '@/api/axios'
import type {
  MembershipTierResponse,
  CreateMembershipTierRequest,
  UpdateMembershipTierRequest,
} from '@/types/membership-tier'
import type { NestedPage } from '@/types/common.types'

export const membershipTierApi = {
  getList: (page = 0, size = 8) =>
    apiClient.get<NestedPage<MembershipTierResponse>>('/loyalty/membership-tiers', {
      params: { page, size, sort: 'id,desc' },
    }),

  getById: (id: number) =>
    apiClient.get<MembershipTierResponse>(`/loyalty/membership-tiers/${id}`),

  //create: (body: CreateMembershipTierRequest) =>
    //apiClient.post<MembershipTierResponse>('/loyalty/membership-tiers', body),

  update: (id: number, body: UpdateMembershipTierRequest) =>
    apiClient.put<MembershipTierResponse>(`/loyalty/membership-tiers/${id}`, body),

  //delete: (id: number) =>
    //apiClient.delete(`/loyalty/membership-tiers/${id}`),
}